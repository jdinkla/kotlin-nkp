#!/bin/bash

ENV_DIR="venv"
mkdir -p "${ENV_DIR}"
python3 -m venv "${ENV_DIR}"
source "${ENV_DIR}"/bin/activate
pip install -r requirements.txt
