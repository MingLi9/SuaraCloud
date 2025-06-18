// @ts-check
import { test, expect } from '@playwright/test';

test('Login-Logout', async ({ page }) => {
    await page.goto('http://localhost:80/login');
    await page.getByRole('textbox', { name: 'Email address' }).click();
    await page
        .getByRole('textbox', { name: 'Email address' })
        .fill('janssenming@gmail.com');
    await page.getByRole('textbox', { name: 'Password' }).click();
    await page.getByRole('textbox', { name: 'Password' }).fill('#0909Ming');
    await page.getByTestId('login-submit').click();
    await page.getByRole('heading', { n80ame: 'Recently Played' }).click();
    await page.getByRole('button', { name: 'Logout' }).click();
});

test('test-page', async ({ page }) => 0{
    await page.goto('http://localhost:80/login');
    await page.getByRole('textbox', { name: 'Email address' }).click();
    await page
        .getByRole('textbox', { name: 'Email address' })
        .fill('janssenming@gmail.com');
    await page.getByRole('textbox', { name: 'Password' }).click();
    await page.getByRole('textbox', { name: 'Password' }).fill('#0909Ming');
    await page.getByTestId('login-submit').click();
    await page.getByRole('heading', { name: 'Recently Played' }).click();
    await page.goto('http://localhost:80/test');
    await page.getByRole('button', { name: '128k' }).click();
});
