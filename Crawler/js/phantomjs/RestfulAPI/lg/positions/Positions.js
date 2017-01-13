function getPositionInfo(companyId, pageNm, page) {

    var filePath = '/ws/github/JavaPlayGround/Crawler/js/phantomjs/RestfulAPI/lg/positions/output/'+ companyId + '-' + pageNm;
    var url = 'https://www.lagou.com/gongsi/searchPosition.json?positionFirstType=技术&companyId=' + companyId + '&pageNo=' + pageNm;

    page.open(url, settings, function (status) {
        var result = JSON.parse(page.plainText);
        fs.write(path, JSON.stringify(result), 'w');
        console.log("write json file to local disk for pn = " + tempPn);
    });
}

function getCompanyPositionInfo(companyId, maxPageNum, page) {
    // var startUrl = 'https://www.lagou.com/gongsi/searchPosition.json?companyId=' + companyId + '&pageNo=1';

    var pageNum = 1;

    var intervalId = setInterval(function (page) {
        getPositionInfo(companyId, pageNum, page);
        pageNum = pageNum + 1;
        if(pageNum > maxPageNum) {
            clearInterval(intervalId);
        }
    }, tickAfter * 1100);
}

function getCityPositionInfo(cityName) {
    var baseDir = '/ws/github/JavaPlayGround/Crawler/js/phantomjs/RestfulAPI/lg/output/' + cityName + '/';

    var fs = require('fs');
    var page = require('webpage').create();

    // @fixme what if the json file not exist?
    for(i = 0; i < 70; i ++) {
        var filePath = baseDir + i + '.json';
        var companiesInfo = fs.read(filePath);
        companiesInfo = JSON.parse(companiesInfo); // convert to json
        var results = companiesInfo['result'];

        for(company in results) {
            var companyId = company['companyId'];
            var metaUrl = 'https://www.lagou.com/gongsi/searchPosition.json?positionFirstType=技术&pageNo=1&companyId=' + companyId

            //@fixme how to make this function blocking???
            page.open(metaUrl, function (status) {
                var result = JSON.parse(page.plainText);
                var totalCount = result['content.data.page.totalCount'];
                var maxPageNum = totalCount / 10 + 1;
                getCompanyPositionInfo(companyId, maxPageNum, page);
            });

        }
    }
}