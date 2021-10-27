require("dotenv").config();
const fs = require("fs");
const path = require("path");

parseFile("frontend.json");
parseFile("backend.json");

function parseFile(filename) {
  const fileStr = fs.readFileSync(path.join(process.cwd(), "keycloak/base", filename));
  const file = JSON.parse(fileStr);
  file.rootUrl = `http://${process.env.SERVER_URL}`;
  fs.writeFileSync(path.join(process.cwd(), "keycloak/utils", filename), JSON.stringify(file));
}
