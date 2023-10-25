import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
//import { UserProfile } from 'Pfad-zur-UserProfile-Schnittstelle';

export interface UserProfile {
  username: string;
  joined: string;
  followed: number;
  following: boolean;
  posts: number;
  followers: number;
  admin: boolean;
  totalSteps: number;
}

@Injectable({
  providedIn: 'root',
})
export class UserSearchService {
  private apiUrl = 'http://ls5vs019.cs.tu-dortmund.de:9001/api/v1';

  constructor(private http: HttpClient) {}

  searchUsers(query: string): Observable<UserProfile[]> {
    let params = new HttpParams().set('q', query);
    params = params.set('limit', '10');
    params = params.set('page', '0');
    return this.http
      .get<{ users: UserProfile[] }>(`${this.apiUrl}/users`, {
        params,
        withCredentials: true,
      })
      .pipe(
        map((response) => response.users) // Extrahieren Sie das users Array aus der Antwort
      );
  }

  /* getPosts(username: string): Observable<PostResponse> {
    const url = `${this.API_URL}user/${username}/posts?limit=10`;
    return this.http.get<PostResponse>(url, { withCredentials: true });
  } */
}
