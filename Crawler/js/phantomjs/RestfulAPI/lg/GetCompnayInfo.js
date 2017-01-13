var page = require('webpage').create();

var url = 'https://www.lagou.com/gongsi/searchPosition.json';

var settings = {
    operation: "POST",
    encoding: "utf-8"
};

page.open('https://www.lagou.com', function (status) {
    // console.log('Status main site : ' + status);
    // console.log(page.json);

    var pn = 1;

    var tickAfter = 6;
    var fs = require('fs');

    var intervalId = setInterval(function () {
        var url = 'https://www.lagou.com/gongsi/6-0-0.json?sortField=1&havemark=0&pn=' + pn;

        page.open(url, settings, function (status) {
            var tempPn = pn;

            var result = JSON.parse(page.plainText);

            // must put the phantom.exit inside the callback function because callback is async
            var path = '/ws/github/JavaPlayGround/Crawler/js/phantomjs/RestfulAPI/lg/output/hangzhou/' + tempPn + ".json";

            fs.write(path, JSON.stringify(result), 'w');

            console.log("write json file to local disk for pn = " + tempPn);

            if(tempPn > 70) {
                clearInterval(intervalId);
            }
        });

        pn = pn + 1;

    }, tickAfter * 1100);

    // phantom.exit();

});


// page.open(url, function(status) {
//     console.log('Status company : ' + status);
//     console.log(page.plainText);
//
//     // must put the phantom.exit inside the callback function because callback is async
//     phantom.exit();
// });

// page.open(url, 'post', data, headers, function (status) {
//     console.log(status);
//
//     if (status !== 'success') {
//         console.log('Unable to get!');
//     } else {
//         var jsonSource = page.plainText;
//
//         // all of the user cars here!!
//         console.log(jsonSource);
//     }
//
//     // then finally close
//     phantom.exit();
// });
