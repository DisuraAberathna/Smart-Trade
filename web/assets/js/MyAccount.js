const loadFeatures = async() => {
    console.log("hw");
    const resp = await fetch("LoadFeatures");

    if (resp.ok) {
        const respObject = await resp.json();
//        if (respObject.success) {
//        } else {
//            
//        }
        console.log(respObject);
    } else {
        document.getElementById("message").innerHTML = "Please try again later!";
    }
};
