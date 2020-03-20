const database = firebase.database();
function toonInfo() {
    id = parent.document.URL.substring(parent.document.URL.indexOf('?')+13, parent.document.URL.length);
    let refStages = database.ref(id + "/stages");
    let refFoodstands = database.ref(id + "/foodstand");
    let refMessages = database.ref(id + "/messages");
    refStages.on('value', gotStages, errData);
    refFoodstands.on('value', gotStages, errData);
    refMessages.on('value', gotStages, errData);
}
function gotStages(data){
    console.log(data.val());
}
function gotFoodstands(data) {
    console.log(data.val())
}
function gotMessages(data) {
    console.log(data.val());
}

function errData(err) {
    console.log("Error!");
    console.log(err);
}
