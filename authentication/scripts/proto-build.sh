#!/bin/bash

# Directory containing the .proto files
PROTO_DIR="./proto"

# Output directory for generated Go code
OUTPUT_DIR="./pb"

# Check if the output directory exists, if not, create it
if [ ! -d "$OUTPUT_DIR" ]; then
  mkdir -p "$OUTPUT_DIR"
  echo "Created output directory $OUTPUT_DIR"
fi

# Run protoc for each .proto file in the directory
for file in $PROTO_DIR/*.proto; do
  filename=$(basename -- "$file")
  filename_noext="${filename%.*}"

  echo "Generating code for $filename_noext"
  protoc --go_out=$OUTPUT_DIR --go-grpc_out=$OUTPUT_DIR $file
done

echo "Code generation complete"
