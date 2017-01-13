var page = require('webpage').create();

page.open('https://www.google.com.hk', function (status) {
    console.log("Status: " + status);
    if(status === "success") {
        // 截图到 example.jpg, 好 NB
        page.render('example.jpg')
    }

    phantom.exit();
});