const BASE_URL = import.meta.env.VITE_API_BASE_URL;

function getToken()
{
    return localStorage.getItem('authToken');
}

export function saveToken(token)
{
    localStorage.setItem('authToken', token);
}

export function removeToken()
{
    localStorage.removeItem('authToken');
    localStorage.removeItem('user');
}

function getAuthHeaders()
{
    const token = getToken();
    const headers = {
        'Content-Type': 'application/json',
    };

    if (token)
    {
        headers['Authorization'] = `Bearer ${token}`;
    }

    return headers;
}

export function isAuthenticated()
{
    if (getToken())
    {
        return true;
    } else
    {
        return false;
    }
}

// get all users
export async function fetchUsers()
{
    try
    {
        const response = await fetch(`${BASE_URL}/allUsers`, {
            headers: getAuthHeaders(),
        });

        if (!response.ok)
        {
            throw new Error('Network error');
        }

        return await response.json();
    } catch (error)
    {
        console.error("Error fetching users:", error);
        throw error;
    }
}

// login a user
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
        { }

        if (response.ok && data && data.token)
        {
            saveToken(data.token);
            localStorage.setItem('user', JSON.stringify(data.user));
        }

        console.log("dataaaa", data)
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

// create a user
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

        if (response.ok && data && data.token)
        {
            saveToken(data.token);
            localStorage.setItem('user', JSON.stringify(data.user));
        }

        // console.log(token)
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

// delete a user
export async function deleteUser(userId)
{
    try
    {
        const response = await fetch(`${BASE_URL}/delete/${userId}`, {
            method: 'DELETE',
            headers: getAuthHeaders(),
        });

        if (!response.ok)
        {
            throw new Error('Network error');
        }

        return true;
    } catch (error)
    {
        console.error("Error deleting user:", error);
        throw error;
    }
}

