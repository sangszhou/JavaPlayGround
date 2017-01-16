var jobDone = false;

function waitFor(testFx, onReady, timeOutMillis) {
    var maxtimeOutMillis = timeOutMillis ? timeOutMillis : 3000, //< Default Max Timout is 3s
        start = new Date().getTime(),
        condition = false,
        interval = setInterval(function () {
            if ((new Date().getTime() - start < maxtimeOutMillis) && !condition) {
                // If not time-out yet and condition not yet fulfilled
                condition = (typeof(testFx) === "string" ? eval(testFx) : testFx()); //< defensive code
            } else {
                if (!condition) {
                    // If condition still not fulfilled (timeout but condition is 'false')
                    console.log("'waitFor()' timeout");
                    phantom.exit(1);
                } else {
                    // Condition fulfilled (timeout and/or condition is 'true')
                    console.log("'waitFor()' finished in " + (new Date().getTime() - start) + "ms.");
                    typeof(onReady) === "string" ? eval(onReady) : onReady(); //< Do what it's supposed to do once the condition is fulfilled
                    clearInterval(interval); //< Stop this interval
                }
            }
        }, 250); //< repeat check every 250ms
};

function getPositionInfo(companyId, pageNm, page) {

    var filePath = '/ws/github/JavaPlayGround/Crawler/js/phantomjs/RestfulAPI/lg/positions/output/' + companyId + '-' + pageNm;
    var url = 'https://www.lagou.com/gongsi/searchPosition.json?positionFirstType=技术&companyId=' + companyId + '&pageNo=' + pageNm;

    page.open(url, settings, function (status) {
        if (status != 'success') {
            console.log("failed to open " + url)
        }

        var result = JSON.parse(page.plainText);
        fs.write(path, JSON.stringify(result), 'w');
        console.log("write json file to local disk for pn = " + pageNm);
    });
}

function getCompanyPositionInfo(companyId, maxPageNum, page) {
    var tickAfter = 6

    var pageNum = 1;

    console.log("get company position info for company id " + companyId + ' max page number ' + maxPageNum);

    var intervalId = setInterval(function () {
        getPositionInfo(companyId, pageNum, page);
        pageNum = pageNum + 1;
        if (pageNum > maxPageNum) {
            clearInterval(intervalId);
        }
    }, tickAfter * 1100);
}

function getCompanyInfo(companyId, page) {
    var metaUrl = 'https://www.lagou.com/gongsi/searchPosition.json?positionFirstType=技术&pageNo=1&companyId=' + companyId

    //@fixme how to make for wait for callback to finish before starting another loop ???
    // I don't want this `open` executed in parallel
    page.open(metaUrl, function (status) {
        console.log("status: " + status);
        var result = JSON.parse(page.plainText);
        console.log("result for company is " + result);

        var totalCount = result.content.data.page.totalCount;
        // var totalCount = result['content.data.page.totalCount'];
        console.log('total count for company id' + companyId + ' is ' + totalCount);

        var maxPageNum = totalCount / 10 + 1;

        var temp = {};
        temp['companyId'] = companyId;
        temp['maxPageNum'] = page;
    });
}

// return string[]
function loadCompaniesInCity(cityName) {
    var baseDir = '/ws/github/JavaPlayGround/Crawler/js/phantomjs/RestfulAPI/lg/output/' + cityName + '/';

    var fs = require('fs');
    var companies = new Array();

    // load all the company id to memory
    for (var i = 2; i < 70; i++) {
        var filePath = baseDir + i + '.json';

        //api: http://www.tuicool.com/articles/nieEVv
        var companiesInfo;
        if (fs.isFile(filePath)) {
            companiesInfo = fs.read(filePath);
        } else {
            continue;
        }

        companiesInfo = JSON.parse(companiesInfo); // convert to json
        var results = companiesInfo['result'];

        for (var idx = 0; idx < results.length; idx++) {
            companies.push(results[idx]['companyId']);
        }

    }

    console.log("total company number is " + companies.length);
    console.log(companies);

    return companies;
}


// return {companyId, maxPageNum}
function calculatePositionsForCompany(cityName) {
    var companies = loadCompaniesInCity(cityName);
    var page = require('page').create();


}
function getCityPositionInfo(cityName) {
    var baseDir = '/ws/github/JavaPlayGround/Crawler/js/phantomjs/RestfulAPI/lg/output/' + cityName + '/';

    var fs = require('fs');
    var page = require('webpage').create();

    console.log("before enter city loop");


    // @fixme what if the json file not exist?

    for (var i = 2; i < 70; i++) {

        var filePath = baseDir + i + '.json';
        var companiesInfo = fs.read(filePath);
        companiesInfo = JSON.parse(companiesInfo); // convert to json
        var results = companiesInfo['result'];

        console.log('file path is ' + filePath);

        for (var idx = 0; idx < results.length; idx++) {
            var companyId = results[idx]['companyId'];

            console.log('get info for company id is ' + companyId);

            var metaUrl = 'https://www.lagou.com/gongsi/searchPosition.json?positionFirstType=技术&pageNo=1&companyId=' + companyId

            //@fixme how to make for wait for callback to finish before starting another loop ???
            // I don't want this `open` executed in parallel
            page.open(metaUrl, function (status) {
                console.log("status: " + status);
                var result = JSON.parse(page.plainText);
                console.log("result for company is " + result);

                var totalCount = result.content.data.page.totalCount;
                // var totalCount = result['content.data.page.totalCount'];
                console.log('total count for company id' + companyId + ' is ' + totalCount);
                var maxPageNum = totalCount / 10 + 1;
                getCompanyPositionInfo(companyId, maxPageNum, page);
            });

            waitFor(function () {
                return jobDone == true;
            });

            jobDone = false;

        }
    }
}

var cityName = 'hangzhou';

loadCompaniesInCity(cityName);
