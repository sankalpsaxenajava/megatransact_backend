echo "Building backend docker image."
sudo docker build --rm -t megatransact-backend .

echo "Cleaning untagged docker images."
sudo docker images -q -f "dangling=true" | sudo xargs --no-run-if-empty docker rmi -f
