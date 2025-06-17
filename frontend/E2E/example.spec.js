// @ts-check
import { test, expect } from '@playwright/test';

test('Login-Logout', async ({ page }) => {
    await page.goto('http://localhost:3000/login');
    await page.getByRole('textbox', { name: 'Email address' }).click();
    await page
        .getByRole('textbox', { name: 'Email address' })
        .fill('janssenming@gmail.com');
    await page.getByRole('textbox', { name: 'Password' }).click();
    await page.getByRole('textbox', { name: 'Password' }).fill('#0909Ming');
    await page.getByTestId('login-submit').click();
    await page.getByRole('heading', { name: 'Recently Played' }).click();
    await page.getByRole('button', { name: 'Logout' }).click();
});

test('test-page', async ({ page }) => {
    await page.goto('http://localhost:3000/login');
    await page.getByRole('textbox', { name: 'Email address' }).click();
    await page
        .getByRole('textbox', { name: 'Email address' })
        .fill('janssenming@gmail.com');
    await page.getByRole('textbox', { name: 'Password' }).click();
    await page.getByRole('textbox', { name: 'Password' }).fill('#0909Ming');
    await page.getByTestId('login-submit').click();
    await page.getByRole('heading', { name: 'Recently Played' }).click();
    await page.goto('http://localhost:3000/test');
    await page.getByRole('button', { name: '128k' }).click();
});
