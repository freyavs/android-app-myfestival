const database = firebase.database();

/*function klik() {
    let festival = {
        naam: "Rock Werchter",
        plaats: "Werchter",
        startDatum: new Date().toString(),
        eindDatum: new Date(2020,2,20).toString(),
        stages:{

        },
        foodStand: {

        },
        messages: {

        }
    };
    firebaseRef.push(festival);
}*/

/*function klik2() {
    let stages = firebaseRef.child('-M2jo8vUpJf0Dw6aJTUs/messages');
    stages.push({
        titel: "titel",
        bericht: "een lange tekst dat wat meer uitleg geeft."
    });
}*/

function festivalLaden() {
    let ref = database.ref();
    ref.on('value', gotFestivals, errData);
}
function gotFestivals(data) {
    let divFestival = document.getElementById('id_festivals');
    let festivals = data.val();
    let innerHTML="";
    if(festivals !== null) {
        let keys = Object.keys(festivals);
        keys.forEach(key => {
            let naam = festivals[key].name;
            let plaats = festivals[key].location;
            let startdatum = new Date(festivals[key].startdate).toLocaleDateString().replace(/-/g, '/');
            let einddatum = new Date(festivals[key].enddate).toLocaleDateString().replace(/-/g, '/');
            innerHTML +=
                "<div class='row'>" +
                "<div class='col-md-12'>" +
                "<div class='card'>" +
                "<h3 class='card-header'>" + naam + "</h3>" +
                "<div class=\"card-body\">\n" +
                "<h5 class=\"card-title\">Extra info</h5>" +
                "<p class=\"card-text\">Plaats: " + plaats + "</p>" +
                "<p class=\"card-text\">Periode: " + startdatum + " - " + einddatum + "</p>" +
                "<a href=\'infoFestival.html?id_festival\=" + key + "\' class='btn btn-primary'>Meer info</a>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "</div><br>";
        });
    }
    innerHTML += "<div class='row'><div class='col-md-12'><button class='btn btn-success btn-block'  data-toggle=\"modal\" data-target=\"#exampleModal\">Toevoegen</button></div></div>";
    divFestival.innerHTML = innerHTML;

}

function errData(err) {
    console.log("Error!");
    console.log(err);
}

function test() {
    let naam = document.getElementById("id_naam").value;
    let plaats = document.getElementById('id_plaats').value;
    let startdatum = document.getElementById('id_startdatum').value;
    let einddatum = document.getElementById('id_einddatum').value;
    if(naam !=='' && plaats !== '' && startdatum !== '' && einddatum !== ''){
        ref = database.ref();
            let festival = {
                name: naam,
                location: plaats,
                startdate: new Date(startdatum).toString(),
                enddate: new Date(einddatum).toString()
            };
        ref.push(festival);
    }
}

