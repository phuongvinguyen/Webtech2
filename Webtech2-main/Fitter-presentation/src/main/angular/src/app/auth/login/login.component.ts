import {
  Component,
  ElementRef,
  Renderer2,
  EventEmitter,
  Input,
  Output,
} from '@angular/core';

import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { GlobalService } from 'src/app/global.service';
import { Router } from '@angular/router';
import { AppComponent } from 'src/app/app.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.sass'],
})
export class LoginComponent {
  loginForm = new FormGroup({
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(0),
    ]),
    //TODO: Validators tauschen
    password: new FormControl('', [Validators.minLength(0)]),
  });

  @Input()
  public authService!: AuthService;

  @Output()
  public loggedIn = new EventEmitter<void>();

  public username = '';
  public password = '';
  public cookie = '';
  public errorMessage: string = '';

  constructor(
    private http: HttpClient,
    private elementRef: ElementRef,
    private renderer: Renderer2,
    private router: Router,
    private globalService: GlobalService,
    private appcomponent: AppComponent
  ) {}

  openModal() {
    this.renderer.addClass(
      this.elementRef.nativeElement.querySelector('#errorModal'),
      'show'
    );
    this.renderer.setStyle(
      this.elementRef.nativeElement.querySelector('#errorModal'),
      'display',
      'block'
    );
  }

  closeModal() {
    this.renderer.removeClass(
      this.elementRef.nativeElement.querySelector('#errorModal'),
      'show'
    );
    this.renderer.removeStyle(
      this.elementRef.nativeElement.querySelector('#errorModal'),
      'display'
    );
  }

  login(e: Event) {
    e.preventDefault();
    this.errorMessage = '';

    // Sicherstellen, dass die Werte nicht null oder undefined sind
    this.username = this.loginForm.value.username || '';
    this.password = this.loginForm.value.password || '';

    //Error Abfangen Funktioniert nicht
    // console.log(this.username);
    // console.log(this.password);

    // const url = 'http://ls5vs019.cs.tu-dortmund.de:9001/api/v1/user/login'; // Replace with your API URL
    const url = 'http://ls5vs019.cs.tu-dortmund.de:9001/api/v1/login'; // Replace with your API URL
    const body = {
      username: this.username,
      password: this.password,
    };
    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      withCredentials: true,
    };

    // console.log(body);

    this.http.post(url, body, options).subscribe({
      next: (response) => {
        // Handle response here
        console.log(response);
      },
      error: (error) => {
        console.log(error);
        this.renderer.setProperty(
          this.elementRef.nativeElement.querySelector('#errorMessage'),
          'textContent',
          error.message || 'Netzwerkfehler'
        );
        this.openModal();
      },
      complete: () => {
        // Code to be executed once the Observable completes
        console.log('Completed');
        this.globalService.login();
        this.router.navigate(['/timeline']);
        this.appcomponent.loadDrawerInformation();
      },
    });
  }

  get canLogin(): boolean {
    return this.username.trim() !== '' && this.password.trim() !== '';
  }
}
