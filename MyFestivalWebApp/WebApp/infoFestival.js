const database = firebase.database();
var id = parent.document.URL.substring(parent.document.URL.indexOf('?')+13, parent.document.URL.length);
function toonInfo() {
    let refStages = database.ref(id + "/stages");
    let refFoodstands = database.ref(id + "/foodstand");
    let refMessages = database.ref(id + "/messages");
    let refFestival = database.ref(id);
    refStages.on('value', gotStages, errData);
    refFoodstands.on('value', gotFoodstands, errData);
    refMessages.on('value', gotMessages, errData);
    refFestival.on('value', gotMap,errData)
}

function gotMap(data) {
    let img = document.getElementById('id_image_map');
    let loc = data.val()['location'].replace('/', "%2F");
    img.src = "https://firebasestorage.googleapis.com/v0/b/myfestival-cf939.appspot.com/o/"+loc+"?alt=media";
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

function uploadMap() {
    let map = document.getElementById('id_map').files.item(0);
    let metadata = {
        contentType: map.type,
    };
    var storageRef = firebase.storage().ref('maps/'+id);
    storageRef.put(map, metadata).then(function (snapshot) {
        ref = database.ref(id);
        let updates= {};
        updates['location'] = "maps/"+id;
        ref.update(updates);
        location.reload();
    })
}
