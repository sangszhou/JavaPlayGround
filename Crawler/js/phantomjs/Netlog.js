// var page = require('webpage').create();
//
// page.onResourceRequested = function (request) {
//     console.log('Request ' + JSON.stringify(request, undefined, 4));
// };
//
// page.onResourceReceived = function (response) {
//     console.log("Receive " + JSON.stringify(response, undefined, 4));
// };

var page = require('webpage').create();
page.onResourceRequested = function(request) {
    console.log('Request ' + JSON.stringify(request, undefined, 4));
};
page.onResourceReceived = function(response) {
    console.log('Receive ' + JSON.stringify(response, undefined, 4));
};

var url = 'http://icam-dev-soa-01:2015/user/xinszhou'

page.open(url)

phantom.exit();