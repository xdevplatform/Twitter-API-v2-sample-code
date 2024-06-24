const needle = require('needle');

// The code below sets the bearer token from your environment variables
// To set environment variables on macOS or Linux, run the export command below from the terminal:
// export BEARER_TOKEN='YOUR-TOKEN'
const token = process.env.BEARER_TOKEN;

const endpointUrl = 'https://api.twitter.com/2/compliance/jobs'

// For User Compliance Job, replace type value with users instead of tweets
// Also replace the name value with your desired job name
const data = {
    "type": "tweets",
    "name": 'my_batch_compliance_job'
}

async function makeRequest() {

    const res = await needle.post(endpointUrl, {
        json: data,
        headers: {
            "User-Agent": "v2BatchComplianceJS",
            "authorization": `Bearer ${token}`
        }
    })

    if (res.body) {
        return res.body;
    } else {
        throw new Error('Unsuccessful request');
    }
}

(async () => {
    try {
        // Make request
        const response = await makeRequest();
        console.dir(response, {
            depth: null
        });

    } catch (e) {
        console.log(e);
        process.exit(-1);
    }
    process.exit();
})();
