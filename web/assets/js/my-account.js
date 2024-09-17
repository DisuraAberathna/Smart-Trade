var modelList;

async function loadFeatures() {

    const response = await fetch(
            "LoadFeatures"
            );

    if (response.ok) {

        const json = await response.json();

        const categoryList = json.categoryList;
        modelList = json.modelList;
        const colorList = json.colorList;
        const storageList = json.storageList;
        const conditionList = json.conditionList;

        loadSelect("categorySelect", categoryList, ["id", "name"]);
        loadSelect("modelSelect", modelList, ["id", "name"]);
        loadSelect("storageSelect", storageList, ["id", "value"]);
        loadSelect("colorSelect", colorList, ["id", "name"]);
        loadSelect("conditionSelect", conditionList, ["id", "name"]);


    } else {
        document.getElementById("message").innerHTML = "Please try again later.";
    }
}

function loadSelect(selectTagId, list, propertyArray) {
    const selectTag = document.getElementById(selectTagId);
    list.forEach(item => {
        let optionTag = document.createElement("option");
        optionTag.value = item[propertyArray[0]];
        optionTag.innerHTML = item[propertyArray[1]];
        selectTag.appendChild(optionTag);
    });
}

function updateModels() {

    let modelSelectTag = document.getElementById("modelSelect");
    modelSelectTag.length = 1;
    
    let selectedCategoryId = document.getElementById("categorySelect").value;

    modelList.forEach(model => {
        if (model.category.id == selectedCategoryId) {
            let optionTag = document.createElement("option");
            optionTag.value = model.id;
            optionTag.innerHTML = model.name;
            modelSelectTag.appendChild(optionTag);
        }
    });
}

async function productListing(){
    const categoryTag =  document.getElementById("categorySelect");
    const modelTag =  document.getElementById("modelSelect");
    const titleTag =  document.getElementById("title");
    const descriptionTag =  document.getElementById("description");
    const storageTag =  document.getElementById("storageSelect");
    const colorTag =  document.getElementById("colorSelect");
    const conditionTag =  document.getElementById("conditionSelect");
    const priceTag =  document.getElementById("price");
    const qtyTag =  document.getElementById("qty");
    const image1Tag =  document.getElementById("image1");
    const image2Tag =  document.getElementById("image2");
    const image3Tag =  document.getElementById("image3");
    
    const messageTag = document.getElementById("message");
    
    const data = new FormData();
    data.append("categoryId",categoryTag.value);
    data.append("modelId",modelTag.value);
    data.append("title",titleTag.value);
    data.append("description",descriptionTag.value);
    data.append("storageId",storageTag.value);
    data.append("colorId",colorTag.value);
    data.append("conditionId",conditionTag.value);
    data.append("price",priceTag.value);
    data.append("quentity",qtyTag.value);
    data.append("image1",image1Tag.files[0]);
    data.append("image2",image2Tag.files[0]);
    data.append("image3",image3Tag.files[0]);
    
    
    
    const response = await fetch(
            "ProductListing",
    {
        method:"POST",
        body:data
    });
    
    if(response.ok){
        
        const json = await response.json();
        
        const popup = Notification();
        
        if(json.success){
            categoryTag.value = 0;
            modelTag.length = 1;
            titleTag.value = "";
            descriptionTag.value = "";
            storageTag.value = 0;
            colorTag.value = 0;
            conditionTag.value = 0;
            priceTag.value = "";
            qtyTag.value = 1;
            image1Tag.value = null;
            image2Tag.value = null;
            image3Tag.value = null;
            
            
            popup.success({
                message:json.content
            });
        }else{
            
            popup.error({
                message:json.content
            });
        }
        
    }else{
        document.getElementById("message").innerHTML = "Please try again later.";
    }
}