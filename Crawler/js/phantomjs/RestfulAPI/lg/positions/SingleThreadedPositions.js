var cityName;
var cityPageNum;

var fileBaseDir = '/ws/github/JavaPlayGround/Crawler/js/phantomjs/RestfulAPI/lg/output/hangzhou';
var fileIndex = 1;
var currentFileName;
var currentFileJson;
var companyIdx;

var currentCompanyPageNn = -1;
var maxPageNum = -1;


var page = require('webpage').create();
var fs = require('fs');

function mainScheduler() {

    var intervalId = setInterval(function () {
        if(fileIndex < 0) {
            // init file
            fileIndex = 2;
            currentFileName = fileBaseDir + fileIndex + '.json';
            var file = fs.read(currentFileName);
            currentFileJson = JSON.parse(file);
            companyIdx = 0;
        }

        if(maxPageNum < 0) {

        }

        var company = currentFileJson[companyIdx];


    });

}