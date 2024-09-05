var modelList;

const loadFeatures = async() => {
    const resp = await fetch("LoadFeatures");

    if (resp.ok) {
        const respObject = await resp.json();

        const categoryList = respObject.categoryList;
        modelList = respObject.modelList;
        const storageSelect = respObject.storageList;
        const colorList = respObject.colorList;
        const conditionList = respObject.conditionList;

        loadSelect("category-select", categoryList, ["id", "name"]);
        loadSelect("model-select", modelList, ["id", "name"]);
        loadSelect("color-select", colorList, ["id", "name"]);
        loadSelect("condition-select", conditionList, ["id", "name"]);
        loadSelect("storage-select", storageSelect, ["id", "value"]);

    } else {
        console.log("error");
    }
};

const loadSelect = (selectTagId, list, propertyArray) => {
    const selectTag = document.getElementById(selectTagId);
    list.forEach(item => {
        let optionTag = document.createElement("option");
        optionTag.value = item[propertyArray[0]];
        optionTag.innerHTML = item[propertyArray[1]];
        selectTag.appendChild(optionTag);    
    });
};

const updateModels = () => {
    const modelTag = document.getElementById("model-select");
    const categoryId = document.getElementById("category-select").value;
    modelTag.length = 1;

    modelList.forEach(model => {
        if (model.category.id == categoryId) {
            let optionTag = document.createElement("option");
            optionTag.value = model.id;
            optionTag.innerHTML = model.name;
            modelTag.appendChild(optionTag);
        }
    });
};

const productListing = async() => {
    const categoryTag = document.getElementById("category-select");
    const modelTag = document.getElementById("model-select");
    const titleTag = document.getElementById("title");
    const descriptionTag = document.getElementById("description");
    const storageTag = document.getElementById("storage-select");
    const colorTag = document.getElementById("color-select");
    const conditionTag = document.getElementById("condition-select");
    const priceTag = document.getElementById("price");
    const qtyTag = document.getElementById("qty");
    const image1Tag = document.getElementById("image1");
    const image2Tag = document.getElementById("image2");
    const image3Tag = document.getElementById("image3");

    const data = new FormData();
    data.append("category_id", categoryTag.value);
    data.append("model_id", modelTag.value);
    data.append("title", titleTag.value);
    data.append("description", descriptionTag.value);
    data.append("storage_id", storageTag.value);
    data.append("color_id", colorTag.value);
    data.append("condition_id", conditionTag.value);
    data.append("price", priceTag.value);
    data.append("qty", qtyTag.value);
    data.append("img1", image1Tag.files[0]);
    data.append("img2", image2Tag.files[0]);
    data.append("img3", image3Tag.files[0]);

    const resp = await fetch(
            "ProductListing",
            {
                method: "POST",
                body: data
            }
    );

    if (resp.ok) {
        const respObject = resp.json();
        if (respObject.success) {
            console.log(respObject.content);
        } else {
            console.log(respObject.content);
        }
    } else {
        console.log("error");
    }
};