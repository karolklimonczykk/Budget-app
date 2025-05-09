import React, { createContext, useState, useContext, ReactNode } from "react";

export interface User {
  id: number;
  email: string;
}

export interface AuthContextType {
  isAuthenticated: boolean;
  login: (token: string) => void;
  logout: () => void;
  user: User | null;
}

// eslint-disable-next-line react-refresh/only-export-components
export const AuthContext = createContext<AuthContextType | undefined>(
  undefined
);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [user, setUser] = useState<User | null>(null);

  const login = (token: string) => {
    localStorage.setItem("accessToken", token);
    setIsAuthenticated(true);

    const parsedUser = parseJwt(token); // <- parsujemy token
    if (parsedUser) {
      setUser({
        id: parsedUser.id,
        email: parsedUser.email,
      });
    } else {
      console.error("Nie udało się sparsować użytkownika z tokena");
      logout(); // jeśli nie uda się odczytać usera, automatycznie wyloguj
    }
  };

  const logout = () => {
    localStorage.removeItem("accessToken");
    setIsAuthenticated(false);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, login, logout, user }}>
      {children}
    </AuthContext.Provider>
  );
};

// eslint-disable-next-line react-refresh/only-export-components
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used within an AuthProvider");
  return context;
};

function parseJwt(token: string): { id: number; email: string } | null {
  try {
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const jsonPayload = decodeURIComponent(
      window
        .atob(base64)
        .split("")
        .map((c) => `%${("00" + c.charCodeAt(0).toString(16)).slice(-2)}`)
        .join("")
    );

    return JSON.parse(jsonPayload);
  } catch (e) {
    console.error("Nie udało się sparsować tokena", e);
    return null;
  }
}
