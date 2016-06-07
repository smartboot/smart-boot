#/bin/bash
curDir=$(cd `dirname $0`; pwd)
find . -name \.settings -exec rm -r {} \;
find . -name \.externalToolBuilders -exec rm -r {} \;
find . -name \.project -exec rm -r {} \;
find . -name \.classpath -exec rm -r {} \;

rm -rf ${curDir}/smart-dal/src/main/resources
cp -R ${curDir}/../../../../../../../smart-dal/src/main/resources/ ${curDir}/smart-dal/src/main/resources
cp -R ${curDir}/../../../../../../../htdocs/ ${curDir}/htdocs

#Mac下的sed命令差异
sed -i "" "s/net\.vinote\.smartboot/\$\{groupId\}/g" `grep  "net.vinote.smartboot" -rl .|grep pom.xml`
sed -i "" "s/smartboot/\${rootArtifactId}/g" `grep  "smartboot" -rl .|grep log4j2.xml`
sed -i "" "s/net\/vinote\/smartboot/\$\{groupId\.replace\(\'\.\', \'\/\'\)\}/g" `grep  "net/vinote/smartboot" -rl .|grep pom.xml `

##修改跟目录下pom.xml
sed -i "" "s/smart-assembly/\$\{artifactId\}-assembly/g" pom.xml
sed -i "" "s/smart-shared/\$\{artifactId\}-shared/g" pom.xml
sed -i "" "s/smart-dal/\$\{artifactId\}-dal/g" pom.xml
sed -i "" "s/smart-component/\$\{artifactId\}-component/g" pom.xml
sed -i "" "s/smart-service-integration/\$\{artifactId\}-service-integration/g" pom.xml
sed -i "" "s/smart-service-impl/\$\{artifactId\}-service-impl/g" pom.xml
sed -i "" "s/smart-service-facade/\$\{artifactId\}-service-facade/g" pom.xml

##修改其余目录下的pom.xml
sed -i "" "s/smart-assembly/\$\{rootArtifactId\}-assembly/g" `grep  "smart-assembly" -rl .|grep pom.xml`
sed -i "" "s/smart-shared/\$\{rootArtifactId\}-shared/g" `grep  "smart-shared" -rl .|grep pom.xml`
sed -i "" "s/smart-dal/\$\{rootArtifactId\}-dal/g" `grep  "smart-dal" -rl .|grep pom.xml`
sed -i "" "s/smart-component/\$\{rootArtifactId\}-component/g" `grep  "smart-component" -rl .|grep pom.xml`
sed -i "" "s/smart-service-integration/\$\{rootArtifactId\}-service-integration/g" `grep  "smart-service-integration" -rl .|grep pom.xml`
sed -i "" "s/smart-service-impl/\$\{rootArtifactId\}-service-impl/g" `grep  "smart-service-impl" -rl .|grep pom.xml`
sed -i "" "s/smart-service-facade/\$\{rootArtifactId\}-service-facade/g" `grep  "smart-service-facade" -rl .|grep pom.xml`

##修改各自pom.xml文件的artifactId
sed -i "" "s/<artifactId>\${artifactId}<\/artifactId>/<artifactId>\${rootArtifactId}-assembly<\/artifactId>/g" ./smart-assembly/pom.xml
sed -i "" "s/<artifactId>\${artifactId}<\/artifactId>/<artifactId>\${rootArtifactId}-shared<\/artifactId>/g" ./smart-shared/pom.xml
sed -i "" "s/<artifactId>\${artifactId}<\/artifactId>/<artifactId>\${rootArtifactId}-dal<\/artifactId>/g" ./smart-dal/pom.xml
sed -i "" "s/<artifactId>\${artifactId}<\/artifactId>/<artifactId>\${rootArtifactId}-component<\/artifactId>/g" ./smart-component/pom.xml
sed -i "" "s/<artifactId>\${artifactId}<\/artifactId>/<artifactId>\${rootArtifactId}-service-integration<\/artifactId>/g" ./smart-service-integration/pom.xml
sed -i "" "s/<artifactId>\${artifactId}<\/artifactId>/<artifactId>\${rootArtifactId}-service-impl<\/artifactId>/g" ./smart-service-impl/pom.xml
sed -i "" "s/<artifactId>\${artifactId}<\/artifactId>/<artifactId>\${rootArtifactId}-service-facade<\/artifactId>/g" ./smart-service-facade/pom.xml
sed -i "" "s/<artifactId>\${artifactId}<\/artifactId>/<artifactId>\${rootArtifactId}-restful<\/artifactId>/g" ./smart-restful/pom.xml
sed -i "" "s/<name>\${artifactId}<\/name>/<name>\${rootArtifactId}-restful<\/name>/g" ./smart-restful/pom.xml