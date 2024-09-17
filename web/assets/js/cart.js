async function loadCartItems() {
    const response = await fetch(
            "LoadCartItems"
            );

    const popup = Notification();

    if (response.ok) {
        const json = await response.json();

        if (json.length == 0) {
            popup.error({
                message: "Your Cart is Empty"
            });
            window.location = "index.html";

        } else {
            
            let cartItemContainer = document.getElementById("cart-item-container");
            let cartItemRow = document.getElementById("cart-item-row");


            cartItemContainer.innerHTML = "";

            let totalQTY = 0;
            let total = 0;
            json.forEach(item => {
                let itemSubTotal = item.product.price * item.qty;
                total += itemSubTotal;
                
                totalQTY += item.qty;
                
                let cartItemClone = cartItemRow.cloneNode(true);
                cartItemClone.querySelector("#cart-item-a").href = "single-product.html?id=" + item.product.id;
                cartItemClone.querySelector("#cart-item-image").src = "product-images/" + item.product.id + "/image1.png";
                cartItemClone.querySelector("#cart-item-title").innerHTML = item.product.title;
                cartItemClone.querySelector("#cart-item-price").innerHTML = item.product.price;
                cartItemClone.querySelector("#cart-item-qty").value = item.qty;

                cartItemClone.querySelector("#cart-item-subtotal").innerHTML = itemSubTotal;

                cartItemContainer.appendChild(cartItemClone);


            });
            document.getElementById("cart-total").innerHTML = total;
            document.getElementById("cart-total-qty").innerHTML = totalQTY;

        }
    } else {
        popup.error({
            message: "Someting wrong"
        });
    }
}