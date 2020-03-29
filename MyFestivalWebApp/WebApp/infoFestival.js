const database = firebase.database();
var id = parent.document.URL.substring(parent.document.URL.indexOf('?')+13, parent.document.URL.length);
function toonInfo() {
    let refStages = database.ref(id + "/stages");
    let refFoodstands = database.ref(id + "/foodstand");
    let refMessages = database.ref(id + "/messages");
    refStages.on('value', gotStages, errData);
    refFoodstands.on('value', gotFoodstands, errData);
    refMessages.on('value', gotMessages, errData);
}
function gotStages(data) {
    let divStages = document.getElementById('id_stages');
    let stages = data.val();
    let innerHTML = "";
    if (stages !== null) {
        let keys = Object.keys(stages);
        keys.forEach(key => {
            let naam = stages[key].name;
            innerHTML +=
                "<p class=\"card-text\">Naam: " + naam + " <br>" +
                "<a href=\'extraInfo.html?id_festival\=" + id + "/stages/" + key + "\' class='btn btn-primary'>Meer info</a>" +
                "</p>";
        });
    }
    innerHTML+="<button class='btn btn-success btn-block'  data-toggle=\"modal\" data-target=\"#addStage\">Toevoegen</button>"
    divStages.innerHTML = innerHTML;
}
function gotFoodstands(data) {
    let divFoodstands = document.getElementById('id_foodstands');
    let foodstands = data.val();
    let innerHTML = "";
    if(foodstands !== null) {
        let keys = Object.keys(foodstands);
        keys.forEach(key => {
            let naam = foodstands[key].name;
            innerHTML +=
                "<p class=\"card-text\">Naam: " + naam + " <br>" +
                "<a href=\'extraInfo.html?id_festival\=" + id + "/foodstand/" + key + "\' class='btn btn-primary'>Meer info</a>" +
                "</p>";
        });
    }
    innerHTML+="<button class='btn btn-success btn-block'  data-toggle=\"modal\" data-target=\"#addFoodstand\">Toevoegen</button>"
    divFoodstands.innerHTML = innerHTML;

}
function gotMessages(data) {
    let divFoodstands = document.getElementById('id_messages');
    let messages = data.val();
    let innerHTML = "";
    if(messages !== null) {
        let keys = Object.keys(messages);
        keys.forEach(key => {
            let titel = messages[key].title;
            let bericht = messages[key].message;
            innerHTML +=
                "<p class=\"card-text\"><b>" + titel + ":</b> <br>" +
                bericht +
                "</p>";
        });
    }
    innerHTML+="<button class='btn btn-success btn-block'  data-toggle=\"modal\" data-target=\"#addMessage\">Toevoegen</button>"
    divFoodstands.innerHTML = innerHTML;
}

function errData(err) {
    console.log("Error!");
    console.log(err);
}

function addStage() {
    let naam = document.getElementById("id_naam_stage").value;
    if(naam !== ''){
        ref = database.ref(id+ "/stages");
        let stage = {
            name: naam
        };
        ref.push(stage);
    }
}
function addFoodstand() {
    let naam = document.getElementById("id_naam_foodstand").value;
    if(naam !== ''){
        ref = database.ref(id+ "/foodstand");
        let foodstand = {
            name: naam
        };
        ref.push(foodstand);
    }
}
function addMessage() {
    let titel = document.getElementById("id_titel_message").value;
    let bericht = document.getElementById("id_bericht_message").value;
    if(titel !== '' && bericht !== ''){
        ref = database.ref(id+"/messages");
        let message = {
            title: titel,
            message: bericht
        };
        ref.push(message);
    }
}
