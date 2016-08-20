DOCKER_IMAGE_NAME = openwes/awskit

.PHONY:	test
test:
	gradle test

.PHONY:	jar
jar:
	gradle clean shadowJar

.PHONY:	docker_build
docker_build: jar
	docker build -t $(DOCKER_IMAGE_NAME) .
