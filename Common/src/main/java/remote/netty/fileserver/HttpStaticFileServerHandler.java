package remote.netty.fileserver;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;
import org.json.JSONException;
import org.json.JSONObject;

import javax.activation.MimetypesFileTypeMap;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;
import static io.netty.handler.codec.http.HttpUtil.setContentLength;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.handler.codec.rtsp.RtspResponseStatuses.NOT_FOUND;
import static org.apache.http.client.utils.URLEncodedUtils.CONTENT_TYPE;


/**
 * Created by xinszhou on 16/01/2017.
 */
public class HttpStaticFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    String baseDir;

    public HttpStaticFileServerHandler(String baseDir) {
        this.baseDir = baseDir;
    }

    // query param used to download a file
    private static final String FILE_QUERY_PARAM = "file";
    boolean readingChunks;

    private HttpPostRequestDecoder decoder;
    private static final HttpDataFactory factory = new DefaultHttpDataFactory(true);

    private static final int THUMB_MAX_WIDTH = 100;
    private static final int THUMB_MAX_HEIGHT = 100;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        URI uri = new URI(request.uri());
        String uriStr = uri.getPath();
        System.out.println(request.method() + " request received");
        if (request.method() == HttpMethod.GET) {
            serveFile(ctx, request); // user requested a file, serve it
        } else if (request.method() == HttpMethod.POST) {
            uploadFile(ctx, request); // user requested to upload file, handle request
        } else {
            // unknown request, send error message
            System.out.println(request.method() + " request received, sending 405");
            sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
        }
    }

    private void serveFile(ChannelHandlerContext ctx, FullHttpRequest request) {

        // decode the query string
        QueryStringDecoder decoderQuery = new QueryStringDecoder(request.getUri());
        Map<String, List<String>> uriAttributes = decoderQuery.parameters();

        // get the requested file name
        String fileName = "";
        try {
            fileName = uriAttributes.get(FILE_QUERY_PARAM).get(0);
        } catch (Exception e) {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST, FILE_QUERY_PARAM + " query param not found");
            return;
        }

        // start serving the requested file
        sendFile(ctx, fileName, request);
    }


    /**
     * This method reads the requested file from disk and sends it as response.
     * It also sets proper content-type of the response header
     *
     * @param fileName name of the requested file
     */
    private void sendFile(ChannelHandlerContext ctx, String fileName, FullHttpRequest request) {
        File file = new File(baseDir + fileName);
        if (file.isDirectory() || file.isHidden() || !file.exists()) {
            sendError(ctx, NOT_FOUND);
            return;
        }

        if (!file.isFile()) {
            sendError(ctx, FORBIDDEN);
            return;
        }

        RandomAccessFile raf;

        try {
            raf = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException fnfe) {
            sendError(ctx, NOT_FOUND);
            return;
        }

        long fileLength = 0;
        try {
            fileLength = raf.length();
        } catch (IOException ex) {
            Logger.getLogger(HttpStaticFileServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
        setContentLength(response, fileLength);
        setContentTypeHeader(response, file);

        //setDateAndCacheHeaders(response, file);
        if (isKeepAlive(request)) {
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }

        // Write the initial line and the header.
        ctx.write(response);

        // Write the content.
        ChannelFuture sendFileFuture;
        DefaultFileRegion defaultRegion = new DefaultFileRegion(raf.getChannel(), 0, fileLength);
        sendFileFuture = ctx.write(defaultRegion);

        // Write the end marker
        ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

        // Decide whether to close the connection or not.
        if (!isKeepAlive(request)) {
            // Close the connection when the whole content is written out.
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static void setContentTypeHeader(HttpResponse response, File file) {
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        mimeTypesMap.addMimeTypes("image png tif jpg jpeg bmp");
        mimeTypesMap.addMimeTypes("text/plain txt");
        mimeTypesMap.addMimeTypes("application/pdf pdf");

        String mimeType = mimeTypesMap.getContentType(file);

        response.headers().set(CONTENT_TYPE, mimeType);
    }

    private void uploadFile(ChannelHandlerContext ctx, FullHttpRequest request) {

        // test comment
        try {
            decoder = new HttpPostRequestDecoder(factory, request);
            //System.out.println("decoder created");
        } catch (HttpPostRequestDecoder.ErrorDataDecoderException e1) {
            e1.printStackTrace();
            sendError(ctx, HttpResponseStatus.BAD_REQUEST, "Failed to decode file data");
            return;
        }

        readingChunks = HttpHeaders.isTransferEncodingChunked(request);

        if (decoder != null) {
            if (request instanceof HttpContent) {

                // New chunk is received
                HttpContent chunk = (HttpContent) request;
                try {
                    decoder.offer(chunk);
                } catch (HttpPostRequestDecoder.ErrorDataDecoderException e1) {
                    e1.printStackTrace();
                    sendError(ctx, HttpResponseStatus.BAD_REQUEST, "Failed to decode file data");
                    return;
                }

                readHttpDataChunkByChunk(ctx);
                // example of reading only if at the end
                if (chunk instanceof LastHttpContent) {
                    readingChunks = false;
                    reset();
                }
            } else {
                sendError(ctx, HttpResponseStatus.BAD_REQUEST, "Not a http request");
            }
        } else {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST, "Failed to decode file data");
        }
    }

    private void sendOptionsRequestResponse(ChannelHandlerContext ctx) {
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private void sendResponse(ChannelHandlerContext ctx, String responseString,
                              String contentType, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(responseString, CharsetUtil.UTF_8));

        response.headers().set(CONTENT_TYPE, contentType);
        response.headers().add("Access-Control-Allow-Origin", "*");

        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }


    private void sendUploadedFileName(JSONObject fileName, ChannelHandlerContext ctx) {
        String msg = "Unexpected error occurred";
        String contentType = "application/json; charset=UTF-8";
        HttpResponseStatus status = HttpResponseStatus.OK;

        if (fileName != null) {
            msg = fileName.toString();
        } else {
            Logger.getLogger(HttpStaticFileServerHandler.class.getName()).log(
                    Level.SEVERE, "uploaded file names are blank");
            status = HttpResponseStatus.BAD_REQUEST;
            contentType = "text/plain; charset=UTF-8";
        }

        sendResponse(ctx, msg, contentType, status);

    }

    private void reset() {
        // destroy the decoder to release all resources
        decoder.destroy();
        decoder = null;
    }

    private void readHttpDataChunkByChunk(ChannelHandlerContext ctx) {
        if (decoder.isMultipart()) {
            try {
                while (decoder.hasNext()) {
                    //System.out.println("decoder has next");
                    InterfaceHttpData data = decoder.next();
                    if (data != null) {
                        writeHttpData(data, ctx);
                        data.release();
                    }
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        } else {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST, "Not a multipart request");
        }
    }

    private void writeHttpData(InterfaceHttpData data, ChannelHandlerContext ctx) {

        if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
            FileUpload fileUpload = (FileUpload) data;

            if (fileUpload.isCompleted()) {
                JSONObject json = saveFileToDisk(fileUpload);
                sendUploadedFileName(json, ctx);
            } else {
                //responseContent.append("\tFile to be continued but should not!\r\n");
                sendError(ctx, HttpResponseStatus.BAD_REQUEST, "Unknown error occurred");
            }
        }
    }

    private JSONObject saveFileToDisk(FileUpload fileUpload) {

        JSONObject responseJson = new JSONObject();

        String filePath; // full path of the new file to be saved
        String uploadedFileName = fileUpload.getFilename();

        // get the extension of the uploaded file
        int i = uploadedFileName.lastIndexOf('.');

        String fileName = uploadedFileName;

        try {
            filePath = baseDir + fileName;

            fileUpload.renameTo(new File(filePath)); // enable to move into another
            responseJson.put("file", fileName);
        } catch (IOException ex) {
            responseJson = null;
        } catch (JSONException ex) {
            Logger.getLogger(HttpStaticFileServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            responseJson = null;
        }
        return responseJson;
    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status, String msg) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        sendError(ctx, status, "Failure: " + status.toString() + "\r\n");
    }


}
