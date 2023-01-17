const needle = require('needle');

// Replace with your job download_url
downloadUrl = ''

async function getRequest() {

    const res = await needle('get', downloadUrl, { compressed: true })

    if (res.body) {
        return res.body.toString('utf8');
    } else {
        throw new Error('Unsuccessful request');
    }
}

(async () => {

    try {
        // Make request
        const response = await getRequest();
        console.dir(response, {
            depth: null
        });

    } catch (e) {
        console.log(e);
        process.exit(-1);
    }
    process.exit();
})();
