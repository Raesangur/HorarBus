const fs = require("fs");
const path = require("path");

copyTo("/server");
copyTo("/docker");

function copyTo(outputDir) {
	fs.copyFileSync(path.join(process.cwd(), ".env"), path.join(process.cwd(), outputDir, ".env"));
}
