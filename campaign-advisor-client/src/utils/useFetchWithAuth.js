// utils/useFetchWithAuth.js
import { useAuth } from "../components/AuthContext";

export const useFetchWithAuth = () => {
  const { token } = useAuth();

  const fetchWithAuth = async (url, method = "GET", body = null) => {
    const headers = {
      "Content-Type": "application/json",
    };

    if (token) {
      headers["Authorization"] = `Bearer ${token}`;
    }

    const requestOptions = {
      method,
      headers,
    };

    if (body) {
      requestOptions.body = JSON.stringify(body);
    }

    const response = await fetch(url, requestOptions);
    return response;
  };

  return fetchWithAuth;
};
