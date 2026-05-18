export const API_CONFIG = {
    BASE_URL:
        import.meta.env.VITE_API_URL
};

export async function apiFetch<T>(
    path: string,
    options?: RequestInit
): Promise<T> {

    const response = await fetch(
        `${API_CONFIG.BASE_URL}${path}`,
        {
            options

        }
    );

    if (!response.ok) {

        throw new Error(
            `API Error: ${response.status}`
        );
    }

    return response.json();
}