var webPage = require('webpage');
var page = webPage.create();

page.open('http://icam-dev-soa-01:2015/user/xinszhou', function(status) {
    console.log('Status: ' + status);
    console.log(page.plainText);

    // must put the phantom.exit inside the callback function because callback is async
    phantom.exit();
});

