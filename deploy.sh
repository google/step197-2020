(cd frontend && npm run-script build --mode=production)
mvn package appengine:deploy -DskipTests