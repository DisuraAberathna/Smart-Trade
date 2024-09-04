const signUp = async() => {
    const reqObject = {
        first_name: document.getElementById("first_name").value,
        last_name: document.getElementById("last_name").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
    };

    const resp = await fetch(
            "SignUp",
            {
                method: "POST",
                body: JSON.stringify(reqObject),
                headers: {
                    "Content-Type": "application/json"
                }
            });

    if (resp.ok) {
        const respObject = await resp.json();
        if (respObject) {
            window.location = "verify-account.html";
        } else {
            document.getElementById("message").innerHTML = respObject.content;
        }
    } else {
        document.getElementById("message").innerHTML = "Please try again later!";
    }
};