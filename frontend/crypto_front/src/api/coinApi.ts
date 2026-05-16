import { apiFetch } from "./client.ts"

export async function getCoins() {

    return apiFetch("/coins")
}