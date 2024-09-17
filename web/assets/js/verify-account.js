async function verifyAccount() {

    const dto = {
        verification: document.getElementById("verification").value
    };

    const response = await fetch(
            "Verification",
            {
                method: "POST",
                body: JSON.stringify(dto),
                headers: {
                    "Content-Type": "application/json"
                }
            });

    if (response.ok) {

        const json = await response.json();

        if (json.success) {
            window.location = "index.html";
        } else {
            document.getElementById("message").innerHTML = json.content;

        }

    } else {
        document.getElementById("message").innerHTML = "Please try again later.";
    }
}