import { defineConfig } from "orval";

export default defineConfig({

    api: {

        input: "http://localhost:8080/v3/api-docs",

        output: {

            target: "./src/generated/api.ts",

            client: "fetch",

            override: {
                mutator: {
                    path: "./src/api/client.ts",

                    name: "apiFetch"
                }
            }
        }
    }
});