var page = require('webpage').create(),
    system = require('system'),
    t, address;

if (system.args.length === 1) {
    console.log('Usage: loadspeed.js <Some url>, don\'t forget to add http/https');
    phantom.exit();
}

t = Date.now();
address = system.args[1];

page.open(address, function (status) {
    if(status != 'success') {
        console.log('FAILED to load the address');
    } else {
        t = Date.now() - t;
        console.log("Loading " + system.args[1]);
        console.log('Loading time ' + t + ' msec');
    }
    phantom.exit();

});