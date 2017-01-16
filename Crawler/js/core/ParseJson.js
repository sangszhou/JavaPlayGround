var jsonPath = '/ws/github/JavaPlayGround/Crawler/js/core/data.json';

var fs = require('fs');

// cannot get field from data directly, need to parse as json first
var data = fs.read(jsonPath);

var jsonStr = JSON.parse(data);
// console.log(jsonStr);

// get field
var pageSize  = jsonStr["totalCount"];

console.log(pageSize);


var obj = JSON.parse('{ "name":"John", "age":30, "city":"New York"}');

console.log(obj["name"]);


// loop json array
var results = jsonStr["result"];

for(idx = 0; idx < results.length; idx ++) {
    var companyId = results[idx]['companyId'];
    console.log(companyId)
}


//get nested field


phantom.exit();

