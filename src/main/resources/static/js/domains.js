var availableDomains = document.getElementById("availableDomains");
var currentDomains = document.getElementById("currentDomains");
var wildcardOverlay = document.getElementById("addWildcardDomain");

// Sets everything up
for (index = 0; index < allDomains.length; index++) {
    wildcard = allDomains[index]["wildcard"]
    allDomains[index] = allDomains[index]['domain'];
    domain = allDomains[index];
    let tag = document.createElement("p");
    tag.innerText = domain;
    if (wildcard) {
        tag.setAttribute("wildcard", "")
        tag.innerText = "*." + tag.innerText;
    }
    tag.setAttribute("domain", domain)
    let clickable = document.createElement("button");
    tag.append(clickable);
    clickable.style = "background-color: #00000000; color: var(--accent_color); border-color: var(--accent_color);"
    clickable.setAttribute("onclick", "buttonCallback(this)")
    if (selectedDomains.includes(domain)) {

    } else {
        clickable.innerText = "+"
        availableDomains.append(tag);
    }
}
for (index = 0; index < selectedDomains.length; index++) {
    domain = selectedDomains[index];
    let tag = document.createElement("p");
    tag.innerText = domain;
    tag.setAttribute("domain", domain);
    let clickable = document.createElement("button");
    tag.append(clickable);
    clickable.style = "background-color: #00000000; color: var(--accent_color); border-color: var(--accent_color);"
    clickable.setAttribute("onclick", "buttonCallback(this)");
    clickable.innerText = "-";
    currentDomains.append(tag);
}

// Callback for the buttons
function buttonCallback(button) {
    let saveAfter = true;
    if (button.parentElement.parentElement == availableDomains) {
        if (button.parentElement.hasAttribute("wildcard")) {
            document.getElementById("wildcardDomain").innerText = button.parentElement.getAttribute("domain");
            document.getElementById("wildcardDomain").setAttribute("domain", button.parentElement.getAttribute("domain"));
            wildcardOverlay.setAttribute("style", "");
            saveAfter = false;
        } else {
            button.innerText = "-";
            availableDomains.removeChild(button.parentElement);
            currentDomains.append(button.parentElement);
        }
    } else if (button.parentElement.parentElement == currentDomains) {
        // If a non wildcard domain, move to selectable domains. Otherwise just remove
        currentDomains.removeChild(button.parentElement);
        if (allDomains.includes(button.parentElement.getAttribute("domain"))
            && !allDomainObjects[allDomains.indexOf(button.parentElement.getAttribute("domain"))]['wildcard']) {
            button.innerText = "+";
            availableDomains.append(button.parentElement);
        }
    }
    if (saveAfter) {
        saveConfigSettings();
    }
}

// Callback for when the user submits a subdomain
function submitWildcard() {
    let sub = document.getElementById("wildcardDomainSubdomain").value;
    let dom = document.getElementById("wildcardDomain").getAttribute("domain");
    document.getElementById("wildcardDomainSubdomain").value = "";
    let fullDomain;
    if (sub.length == 0) {
        fullDomain = dom;
    } else {
        fullDomain = sub + "." + dom;
    }
    let tag = document.createElement("p");
    tag.innerText = fullDomain;
    tag.setAttribute("domain", fullDomain);
    let clickable = document.createElement("button");
    tag.append(clickable);
    clickable.style = "background-color: #00000000; color: var(--accent_color); border-color: var(--accent_color);"
    clickable.setAttribute("onclick", "buttonCallback(this)");
    clickable.innerText = "-";
    currentDomains.append(tag);
    document.getElementById('addWildcardDomain').setAttribute('style', 'display: none;')
    saveConfigSettings();
}

// Function to call api and save settings
function saveConfigSettings() {

    // Get selected domains
    let doms = [];
    for (let i = 0; i < currentDomains.childNodes.length; i++) {
        try {
            doms.push(currentDomains.children[i].getAttribute("domain"));
        } catch (error) {

        }
    }

    // Send update request
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "domainConfig");
    xhr.setRequestHeader(csrf_["headerName"], csrf_["token"]);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status != 200){
                document.getElementById('addWildcardDomain').setAttribute('style', 'display: none;')
                availableDomains.innerText = "Something went wrong while updating your domains: http code " + xhr.status;
                currentDomains.innerText = "Something went wrong while updating your domains: http code " + xhr.status;
            }
        }
    };
    let data = new FormData();
    data.append("domains", JSON.stringify(doms))
    xhr.send(data);
}