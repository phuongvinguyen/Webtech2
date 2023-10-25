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
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.sass'],
})
export class SignupComponent {
  loginForm = new FormGroup({
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(2),
    ]),
    password: new FormControl('', [Validators.minLength(6)]),
  });
  @Input()
  public authService!: AuthService;

  @Output()
  public loggedIn = new EventEmitter<void>();

  public username = '';
  public password = '';
  public errorMessage: string = '';

  constructor(
    private http: HttpClient,
    private elementRef: ElementRef,
    private renderer: Renderer2,
    private router: Router
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
    console.log(this.username);
    console.log(this.password);

    // const url = 'http://ls5vs019.cs.tu-dortmund.de:9001/api/v1/user/login'; // Replace with your API URL
    const url = 'http://ls5vs019.cs.tu-dortmund.de:9001/api/v1/register'; // Replace with your API URL

    const body = {
      username: this.username,
      password: this.password,
    };
    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
    };

    console.log(body);

    this.http.post(url, body, { ...options, responseType: 'text' }).subscribe({
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
        this.router.navigateByUrl('/login');
      },
    });
  }

  get canLogin(): boolean {
    return this.username.trim() !== '' && this.password.trim() !== '';
  }
}
