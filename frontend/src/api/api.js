const BASE_URL = import.meta.env.VITE_API_BASE_URL;

export async function fetchUsers()
{
    try
    {
        const response = await fetch(`${BASE_URL}/allUsers`);
        if (!response.ok)
        {
            throw new Error('Network error');
        }

        console.log(response)
        return await response.json();
    } catch (error)
    {
        console.error("Error fetching users:", error);
        throw error;
    }
}

export async function loginUser(credentials)
{
    try
    {
        const response = await fetch(`${BASE_URL}/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(credentials),
        });

        let data = null;
        try
        {
            data = await response.json();
        } catch (error)
        {

        }

        return {
            ok: response.ok,
            status: response.status,
            data,
        };
    } catch (error)
    {
        console.error("error logging in user:", error);
        throw error;
    }
}

export async function registerUser(userInfo)
{
    try
    {
        const response = await fetch(`${BASE_URL}/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userInfo),
        });

        let data = null;
        try
        {
            data = await response.json();
        } catch (error)
        {

        }

        return {
            ok: response.ok,
            status: response.status,
            data,
        };
    } catch (error)
    {
        console.error("error registering user:", error);
        throw error;
    }
}