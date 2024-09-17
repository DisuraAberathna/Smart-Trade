async function checkSignIn(){
    const response = await fetch(
            "CheckSignIn",
            );

//const popup = Notification();

    if (response.ok) {

        const json = await response.json();
        const response_dto = json.response_DTO;
        
        
        if (response_dto.success) {
            //signed in
            const user = response_dto.content;
            console.log(user);
            
            let st_quick_link = document.getElementById("st-quick-link");
            let st_quick_link_li_1 = document.getElementById("st-quick-link-li-1");
            let st_quick_link_li_2 = document.getElementById("st-quick-link-li-2");
            
            st_quick_link_li_1.remove();
            st_quick_link_li_2.remove();
            
            let new_li_tag1 = document.createElement("li");
            new_li_tag1.innerHTML = user.first_name+" "+user.last_name;
            st_quick_link.appendChild(new_li_tag1);
            
            let st_button_1 = document.getElementById("st-button-1");
            st_button_1.href = "SignOut";
            st_button_1.innerHTML = "Sign Out";
            
            let st_div_1 = document.getElementById("st-div-1");
            st_div_1.remove();
            
        } else {
            //not signed in
            const msg = json.content;
            console.log(msg);

        }
        
        //display last 3 products
        let st_slide_container = document.getElementById("st-slide-container");
        let st_single_slide = document.getElementById("st-single-slide");
        st_slide_container.innerHTML = "";
        
        const productList = json.products;
        
        productList.forEach(product => {
            let st_single_slide_clone = st_single_slide.cloneNode(true);
            st_single_slide_clone.querySelector("#st-product-title").innerHTML = product.title;
            st_single_slide_clone.querySelector("#st-product-link").href = "single-product.html?id="+product.id;
            
            st_slide_container.appendChild(st_single_slide_clone);
            
        });
        
        

    }
    
}

async function viewCart(){
    const response = await fetch("cart.html");
    
    if(response.ok){
        
        const cartHtmlText = await response.text();
        
        const parser = new DOMParser();
        const cartHtml = parser.parseFromString(cartHtmlText, "text/html");
        
        const cart_main = cartHtml.querySelector("#cart-main");
        
        document.querySelector(".main-wrapper").innerHTML = cart_main.innerHTML;
        loadCartItems();
    }
}