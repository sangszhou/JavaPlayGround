var page = require('webpage').create();
var server = require('webserver').create();
var system = require('system');


// avoid yanzhengma url



if (system.args.length !== 2) {
    console.log('Usage: server.js <some port>');
    phantom.exit(1);
} else {

    page.open("http://sh.lianjia.com/", function (status) {
        console.log("start lianjia endpoint with status: " + status);
        console.log(page.content)
    });

    console.log("getting port")

    port = system.args[1];
    var listening = server.listen(port, function (request, response) {
        console.log("GOT HTTP REQUEST");

        // post field of original request body
        var postBody = JSON.parse(request.post);
        var redirectUrl = postBody['url'];
        console.log('redirect url is ' + redirectUrl);

        page.open(redirectUrl, function (status) {
            if (status === 'success') {
                response.statusCode = 200;
                response.write(page.content);
                response.close();
            } else {
                console.log("failed to parse " + redirectUrl);
                response.statusCode = 400;
                response.write('failed to parse ' + redirectUrl);
                response.close();
            }
        });
    });

    if (!listening) {
        console.log("could not create web server listening on port " + port);
        phantom.exit();
    }
}