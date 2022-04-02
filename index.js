const Captcha = require("2captcha");
const axios = require("axios");

// Get access to solver by passing your API key
const captchaSolver = new Captcha.Solver("93237cc64176f47642d2219fc8938e1e");

// Bypass Captcha function
const bypassCaptcha = async () => {
    console.log("Waiting for response...");

    // Send the captcha solution to the server
    try {
        // Get the captcha image and solve it using 2Captcha

        const { data } = await captchaSolver.hcaptcha(
            "7f3fbc5b-8766-491d-b278-34e6fbefba2a",
            "https://www.mewatch.sg/sa2022/voting/top10male/votingapp/signup/MostPopularMAV?entryId=2382844"
        );
        console.log(data);

        var trans_unique_id = "7olqjt9ksd3ibt8a10rri4l8h9";
        var body = "g-recaptcha-response=" + data + "&url=addvote&entry_id=2382844&promotion_id=185627&trans_unique_id=" + trans_unique_id + "&fingerprintjsdecry=884c0b6817da8f963feb51d0e9c66f5d&fingerprintjsenc=LQMfFSy27wQnOV8ys6W6KehKXCtzleBfH%252FZV2zEKjQE%253D&cookie_id=c329afe9dc91106e17dcc384d8eaee4b&source=desktop&user_id=4077424";
        console.log(body);

        let response = await axios.post(
            "https://graph.votigo.com/index.php?&hostingUrl=https%3A%2F%2Fmewatch.sg%2Fsa2022%2Fvoting%2Ftop10male&uvmk=52b5f9056058ec2598fd31f502c99c6e&transuvs=7olqjt9ksd3ibt8a10rri4l8h4",
            body, {
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            }
        });

        // Print the response
        console.log(response.data);
    } catch (err) {
        console.log(err);
    }
};

// Run the function
bypassCaptcha();
