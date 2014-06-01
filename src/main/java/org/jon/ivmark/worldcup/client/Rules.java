package org.jon.ivmark.worldcup.client;

public class Rules {

    public static String rulesHtml() {
        return "Du kan tippa max 72 rader per omgång, och måste tippa varje match för att kunna " +
                "spara din rad." +
                "<p>Du kan ändra dina rader hur många gånger som helst fram tills VM startar. " +
                "<b>Deadline för ändringar är 2014-06-12 21:59.</b></p>" +
                "<h2>Poäng</h2>" +
                "För att få poäng i en omgång måste du få minst 11 rätt. Bara din bästa rad räknas " +
                "för poäng. Poäng fördelas följande:<p>" +
                "<table class=rulesTable>" +
                "<tr><td><b>Antal rätt</b></td><td><b>Total poängpott</b></td></tr>" +
                "<tr><td>16</td><td>100 000</td></tr>" +
                "<tr><td>15</td><td>50 000</td></tr>" +
                "<tr><td>14</td><td>30 000</td></tr>" +
                "<tr><td>13</td><td>20 000</td></tr>" +
                "<tr><td>12</td><td>15 000</td></tr>" +
                "<tr><td>11</td><td>10 000</td></tr>" +
                "</table></p>" +
                "Din poäng blir poängpotten dividerat med antalet spelare (inklusive dig själv)" +
                " som hade minst lika många rätt." +
                "<p>Exempel:" +
                "<ul>" +
                "<li>Du är ensam om att få 16 rätt. Du får 100 000 poäng.</li>" +
                "<li>Du är en av fem som fick 16 rätt. Du får 20 000 poäng.</li>" +
                "<li>Du är en av tre som fick 15 rätt, en spelare fick 16 rätt. Du får 11 250 poäng.</li>" +
                "<li>Du är en av åtta som fick 14 rätt, ingen spelare fick fler än 14 rätt. Du får 3 750 poäng.</li>" +
                "<li>Du fick 11 rätt, 9 andra spelare fick 11 eller fler rätt. Du får 1 000 poäng.</li>" +
                "<li>Du fick 10 rätt. Du får 0 poäng.</li>" +
                "</ul></p>";
    }
}
