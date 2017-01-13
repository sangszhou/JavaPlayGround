// use strict
var AUTH_ENDPOINT = 'http://192.168.1.251';
var SERV_ENDPOINT = 'http://192.168.1.250';
var returnedData = '';

var page = require('webpage').create(),
    server = AUTH_ENDPOINT + '/services/users/login',
    settings = {
        operation: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        data: JSON.stringify({'user': 'myusername', 'pword': '123456789'})
    };

page.onResourceRequested = function (requestData, networkRequest) {
    console.log('Request (#' + requestData.id + '): ');
    console.log(JSON.stringify(requestData));
};

page.onResourceReceived = function (response) {
    console.log('Response (#' + response.id + ', stage "' + response.stage + '"): ' + JSON.stringify(response));
};

page.onLoadFinished = function (status) {
    console.log('Status: ' + status);
    // Do other things here...
};
page.onResourceError = function (resourceError) {
    console.log('Unable to load resource (#' + resourceError.id + 'URL:' + resourceError.url + ')');
    console.log('Error code: ' + resourceError.errorCode + '. Description: ' + resourceError.errorString);
};

page.open(server, settings, function (status) {
    console.log('Status: ' + status);
    // when successful the response from server is a JSON
    // like so '{"authtoken":"23423","user":"myname"}'
    returnedData = JSON.parse(page.plainText);
    // this should be my token
    console.log(returnedData.authtoken);
});

// now use this token on this url
var server = SERV_ENDPOINT + '/services/get/list?data=cars';
var settings = {
    operation: "GET",
    encoding: "utf8",
    headers: {
        "token": returnedData.authtoken
    }
};

page.open(server, settings, function (status) {
    console.log(status);
    if (status !== 'success') {
        console.log('Unable to get!');
    } else {
        var jsonSource = page.plainText;
        var resultObject = JSON.parse(jsonSource);
        // all of the user cars here!!
        console.log(resultObject);
    }
    // then finally close
    phantom.exit();
});
