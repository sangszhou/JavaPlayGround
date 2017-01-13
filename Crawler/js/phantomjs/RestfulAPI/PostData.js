var page = require('webpage').create(),
    server = AUTH_ENDPOINT + '/services/users/login',
    settings = {
        operation: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        data: JSON.stringify({'user':'myusername', 'pword':'123456789'})
    };


var server = SERV_ENDPOINT + '/services/get/list?data=cars';

var settings = {
    operation: "GET",
    encoding: "utf8",
    headers: {
        "token": returnedData.authtoken
    }
};

page.open(server, settings, function(status) {
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
