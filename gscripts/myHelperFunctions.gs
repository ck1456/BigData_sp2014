// Removes column from spreadsheet by name
// The subsequent columns are moved to the left accordingly
function removeColumnByName(colName)
{ 
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var sheet = ss.getSheets()[0];

  var headers = sheet.getRange(1, 1, 1, sheet.getLastColumn()).getValues();
  
  Logger.clear();
  var offset = 0;
  for (h in headers[0]){
    if (headers[0][h].indexOf(colName) > -1){
      Logger.log("delete col "+ h + ";" + headers[0][h]);
      sheet.deleteColumn(parseInt(h)+1 - offset); // offset is needed because the table updates after deletion.
      offset += 1;
    }
  }
  
  var recipient = Session.getActiveUser().getEmail();
  var subject = 'Gscript log for Big Data project';
  var body = Logger.getLog();
  MailApp.sendEmail(recipient, subject, body);
}
