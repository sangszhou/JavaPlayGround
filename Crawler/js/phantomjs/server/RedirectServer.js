// handle json response

"use strict";
var page = require('webpage').create();
var server = require('webserver').create();
var system = require('system');
var startUrl = 'https://www.lagou.com';

var host, port;

if (system.args.length !== 2) {
    console.log('Usage: server.js <some port>');
    phantom.exit(1);
} else {
    page.open(startUrl, function (status) {
        console.log("start lagou endpoint with status: " + status);
    });

    port = system.args[1];

    var listening = server.listen(port, function (request, response) {

        console.log("GOT HTTP REQUEST");
        console.log(JSON.stringify(request, null, 4));

        var postBody = JSON.parse(request.post);

        var redirectUrl = 'https://' + postBody['url'];

        console.log('redirect url is ' + redirectUrl);
        page.open(redirectUrl, function (status) {
            if (status === 'success') {
                //not thread safe obviously
                var result = JSON.parse(page.plainText);

                console.log("get result from redirect url: " + JSON.stringify(result));

                response.headers = {"Cache": "no-cache", "Content-Type": "application/json;charset=UTF-8"};
                response.statusCode = 200;
                response.write(JSON.stringify(result));
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

    // var url = "http://localhost:" + port + "/foo/bar.php?asdf=true";
    // console.log("SENDING REQUEST TO:");
    // console.log(url);
    //
    // page.open(url, function (status) {
    //     console.log("client receive status: " + status);
    //     if (status !== 'success') {
    //         console.log('FAIL to load the address');
    //     } else {
    //         console.log("GOT REPLY FROM SERVER:");
    //         console.log(page.content);
    //     }
    //     phantom.exit();
    // });
}
