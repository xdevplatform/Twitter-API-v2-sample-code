const got = require('got');
const fs = require('fs');

// Replace with your job download_url
uploadUrl = ''

// Replace with your file path that contains the list of Tweet IDs or User IDs, one ID per line
const readStream = fs.createReadStream('/path/to/file');

async function getRequest() {

    const res = await got.put(uploadUrl, {
        body: readStream,
        headers: {
            "Content-Type": "text/plain"
        }
    })

    if (res.statusCode == 200) {
        return res.statusCode;
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
