const database = firebase.database();
var id = parent.document.URL.substring(parent.document.URL.indexOf('?')+13, parent.document.URL.length);
let wat = (id.includes("/stages/")) ? "/concerten" : "/menu";
function extraInfoLaden() {
    laadModal();
    let refInfo = database.ref(id + wat);
    let gotInfo = (wat === "/concerten") ? gotConcerten : gotMenu;
    refInfo.orderByChild('starttijd').on('value', gotInfo, errData);
}
function laadModal() {
    let modalDiv = document.getElementById("exampleModal");
    let modal = "";
    if(wat === "/concerten"){
        modal += "<!-- Modal -->\n" +
            "       <div class=\"modal-dialog\" role=\"document\">\n" +
            "                <div class=\"modal-content\">\n" +
            "                    <div class=\"modal-header\">\n" +
            "                        <h5 class=\"modal-title\" id=\"exampleModalLabel\">Modal title</h5>\n" +
            "                        <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\n" +
            "                            <span aria-hidden=\"true\">&times;</span>\n" +
            "                        </button>\n" +
            "                    </div>\n" +
            "                    <div class=\"modal-body\">\n" +
            "                        <form>\n" +
            "                          <div class=\"form-group\">" +
            "                                <label for=\"id_artiest\">Naam Artiest</label>\n" +
            "                                <input type=\"text\" id=\"id_artiest\" class=\"form-control\" placeholder=\"Enter artiest\">\n" +
            "                          </div> " +
            "                          <div class=\"form-group\">" +
            "                                <label for=\"id_start\">Starttijd</label>\n" +
            "                                <input type=\"datetime-local\" id=\"id_start\" class=\"form-control\" placeholder=\"Enter starttijd\">\n" +
            "                          </div> " +
            "                          <div class=\"form-group\">" +
            "                                <label for=\"id_eind\">Eindtijd</label>\n" +
            "                                <input type=\"datetime-local\" id=\"id_eind\" class=\"form-control\" placeholder=\"Enter eindtijd\">\n" +
            "                          </div> " +
            "                        </form>\n" +
            "                    </div>\n" +
            "                    <div class=\"modal-footer\">\n" +
            "                        <button type=\"button\" class=\"btn btn-danger\" data-dismiss=\"modal\">Close</button>\n" +
            "                        <button type=\"button\" class=\"btn btn-primary\" onclick=\"maakConcert()\">Save</button>\n" +
            "                    </div>\n" +
            "                </div>\n" +
            "            </div>\n"
    }
    else {
        modal += "<!-- Modal -->\n" +
            "       <div class=\"modal-dialog\" role=\"document\">\n" +
            "                <div class=\"modal-content\">\n" +
            "                    <div class=\"modal-header\">\n" +
            "                        <h5 class=\"modal-title\" id=\"exampleModalLabel\">Modal title</h5>\n" +
            "                        <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\n" +
            "                            <span aria-hidden=\"true\">&times;</span>\n" +
            "                        </button>\n" +
            "                    </div>\n" +
            "                    <div class=\"modal-body\">\n" +
            "                        <form>\n" +
            "                          <div class=\"form-group\">" +
            "                                <label for=\"id_gerecht\">Naam gerecht</label>\n" +
            "                                <input type=\"text\" id=\"id_gerecht\" class=\"form-control\" placeholder=\"Enter gerecht naam\">\n" +
            "                          </div> " +
            "                          <div class=\"form-group\">" +
            "                                <label for=\"id_prijs\">Prijs</label>\n" +
            "                                <input type=\"number\" id=\"id_prijs\" class=\"form-control\" placeholder=\"Enter prijs\">\n" +
            "                          </div> " +
            "                          <div class=\"form-group\">" +
            "                                <label for=\"id_veggie\">Veggie: </label>\n" +
            "                                <input type=\"checkbox\" id=\"id_veggie\">\n" +
            "                          </div> " +
            "                          <div class=\"form-group\">" +
            "                                <label for=\"id_vegan\">Vegan: </label>\n" +
            "                                <input type=\"checkbox\" id=\"id_vegan\">\n" +
            "                          </div> " +
            "                        </form>\n" +
            "                    </div>\n" +
            "                    <div class=\"modal-footer\">\n" +
            "                        <button type=\"button\" class=\"btn btn-danger\" data-dismiss=\"modal\">Close</button>\n" +
            "                        <button type=\"button\" class=\"btn btn-primary\" onclick=\"maakMenu()\">Save</button>\n" +
            "                    </div>\n" +
            "                </div>\n" +
            "            </div>\n"
    }
    modalDiv.innerHTML = modal;
}

function maakConcert() {
    let artiest = document.getElementById("id_artiest").value;
    let start = document.getElementById("id_start").value;
    let eind = document.getElementById("id_eind").value;
    if(artiest !== "" && start !== "" && eind !== ""){
        let ref = database.ref(id + wat);
        let concert = {
            artiest : artiest,
            eindtijd: eind,
            starttijd: start
        };
        ref.push(concert);
    }
}
function maakMenu() {
    let gerecht = document.getElementById("id_gerecht").value;
    let prijs = document.getElementById("id_prijs").value;
    let veggie = document.getElementById("id_veggie").checked;
    let vegan = document.getElementById("id_vegan").checked;
    if(gerecht !== "" && prijs !== ""){
        let ref = database.ref(id + wat);
        let menu = {
            gerecht: gerecht,
            prijs: prijs,
            veggie: veggie,
            vegan: vegan
        };
        ref.push(menu);
    }
}
function gotConcerten(data) {
    let info = data.val();
    let divInfo = document.getElementById("id_divInfo");
    let innerHTML = "";


    if (info !== null) {
        let keys = Object.keys(info);
        keys.forEach(key => {
            let artiest = info[key].artiest;
            let einddatum = new Date(info[key].eindtijd).toLocaleString().replace(/-/g,'/');
            let startdatum = new Date(info[key].starttijd).toLocaleString().replace(/-/g,'/');
            innerHTML +=
                "<div class='row'>" +
                "<div class='col-md-12'>" +
                "<div class='card'>" +
                "<h3 class='card-header'>" + artiest + "</h3>" +
                "<div class=\"card-body\">\n" +
                "<h5 class=\"card-title\">Extra info</h5>" +
                "<p class=\"card-text\">Periode: " + startdatum + " - " + einddatum + "</p>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "</div><br>";
        });
    }
    innerHTML += "<div class='row'><div class='col-md-12'><button class='btn btn-success btn-block'  data-toggle=\"modal\" data-target=\"#exampleModal\">Toevoegen</button></div></div>";
    divInfo.innerHTML = innerHTML;
}

function gotMenu(data) {
    let info = data.val();
    let divInfo = document.getElementById("id_divInfo");
    let innerHTML = "";
    if (info !== null) {
        let keys = Object.keys(info);
        keys.forEach(key => {
            let gerecht = info[key].gerecht;
            let prijs = info[key].prijs;
            let vegan = info[key].vegan;
            let veggie = info[key].veggie;
            innerHTML +=
                "<div class='row'>" +
                "<div class='col-md-12'>" +
                "<div class='card'>" +
                "<h3 class='card-header'>" + gerecht + "</h3>" +
                "<div class=\"card-body\">\n" +
                "<h5 class=\"card-title\">Extra info</h5>" +
                "<p class=\"card-text\">Prijs: €" + prijs + "</p>" +
                "<p class=\"card-text\">Veggie: <input disabled type='checkbox'";
            if(veggie)
                innerHTML += " checked ";
            innerHTML +="></p>" +
                "<p class=\"card-text\">Vegan: <input disabled type='checkbox'";
            if(vegan)
                innerHTML += " checked ";

            innerHTML += "></p>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "</div><br>";
        });
    }
    innerHTML += "<div class='row'><div class='col-md-12'><button class='btn btn-success btn-block'  data-toggle=\"modal\" data-target=\"#exampleModal\">Toevoegen</button></div></div>";
    divInfo.innerHTML = innerHTML;
}


function errData(err) {
    console.log("Error!");
    console.log(err);
}
