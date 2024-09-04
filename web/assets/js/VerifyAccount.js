const verifyAccount = async() => {
    const reqObject = {
        verification: document.getElementById("verification").value,
    };

    const resp = await fetch(
            "Verification",
            {
                method: "POST",
                body: JSON.stringify(reqObject),
                headers: {
                    "Content-Type": "application/json"
                }
            });

    if (resp.ok) {
        const respObject = await resp.json();
        if (respObject.success) {
            window.location = "index.html";
        } else {
            document.getElementById("message").innerHTML = respObject.content;
        }
    } else {
        document.getElementById("message").innerHTML = "Please try again later!";
    }
};
