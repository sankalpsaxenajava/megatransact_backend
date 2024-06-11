echo "Building backend docker image."
docker build --rm -t megatransact-backend .

echo "Cleaning untagged docker images."
docker images -q -f "dangling=true" | xargs --no-run-if-empty docker rmi -f
