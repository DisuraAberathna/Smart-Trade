const signIn = async() => {
    const reqObject = {
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
    };

    const resp = await fetch(
            "SignIn",
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
            if (respObject.content === "Not Verified") {
                window.location = "verify-account.html"
            } else {
                document.getElementById("message").innerHTML = respObject.content;
            }
        }
    } else {
        document.getElementById("message").innerHTML = "Please try again later!";
    }
};
