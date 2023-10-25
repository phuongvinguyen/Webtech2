import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AppComponent } from 'src/app/app.component';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.sass'],
})
export class SettingsComponent {
  constructor(
    private http: HttpClient,
    private router: Router,
    private app: AppComponent
  ) {}

  password = '';

  passwortForm = new FormGroup({
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(0),
    ]),
    //TODO: Validators tauschen
    password: new FormControl('', [Validators.minLength(6)]),
  });

  changePassword(e: Event) {
    e.preventDefault();

    this.password = this.passwortForm.value.password || '';

    const username = this.app.user!.username;

    const url =
      'http://ls5vs019.cs.tu-dortmund.de:9001/api/v1/user/' + username;
    const body = {
      password: this.password,
    };
    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      withCredentials: true,
    };

    this.http.put(url, body, options).subscribe({
      next: (response) => {
        console.log(response);
      },
      error: (error) => {
        console.log(error);
      },
      complete: () => {
        console.log('Completed');
        this.router.navigate(['/timeline']);
      },
    });
  }
}
