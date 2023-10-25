import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';

export abstract class AuthService {
  protected _isLoggedIn: BehaviorSubject<boolean> =
    new BehaviorSubject<boolean>(false);

  constructor(protected http: HttpClient) {}

  abstract login(username: string, password: string): Observable<boolean>;

  abstract logout(): Observable<boolean>;

  abstract getAuthHeaders(): HttpHeaders;

  abstract getBaseUrl(): string;

  get isLoggedIn$(): Observable<boolean> {
    return this._isLoggedIn.asObservable();
  }
}
